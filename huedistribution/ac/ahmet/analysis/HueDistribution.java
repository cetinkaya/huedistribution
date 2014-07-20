/**
   HueDistribution by Ahmet Cetinkaya
   
   Compute distribution of "hue" for segments of
   a number of images by the description given in a
   data file obtained using idext.
   
   This program is based on examples provided
   in hadoop distribution.
*/


package ac.ahmet.analysis;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class HueDistribution {

  public static class HueMapper extends Mapper<Object, Text, Text, IntWritable>{
    
    private final static IntWritable one = new IntWritable(1);
    private Text hueRange = new Text();
      
    public void map(Object key, 
                    Text value, 
                    Context context
                    ) throws IOException, InterruptedException {
        String dataLine = value.toString();
        String[] columns = dataLine.split(",");
        String hueStr = columns[15];
        double hue = Double.parseDouble(hueStr);

        // HUE is basically an angle taking values in 0 to 360
        // We will consider 10 ranges scaled between 0 and 1
        for (int i = 1; i <= 10; i++) {
            double angle = i * 36;
            if (hue <= angle) {
                String range = "0." + Integer.toString(i-1) + " - " + "0." + Integer.toString(i); 
                hueRange.set(range);
                context.write(hueRange, one);
                break;
            }
        }
    }
  }
  
  public static class CountReducer extends Reducer<Text,IntWritable,Text,IntWritable> {
    private IntWritable result = new IntWritable();

    public void reduce(Text key, 
                       Iterable<IntWritable> values, 
                       Context context
                       ) throws IOException, InterruptedException {
      int sum = 0;
      for (IntWritable val : values) {
        sum += val.get();
      }
      result.set(sum);
      context.write(key, result);
    }
  }

  public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
    if (otherArgs.length != 2) {
      System.err.println("Usage: huedistribution <in> <out>");
      System.exit(2);
    }
    Job job = new Job(conf, "huedistribution");
    job.setJarByClass(HueDistribution.class);
    job.setMapperClass(HueMapper.class);
    job.setCombinerClass(CountReducer.class);
    job.setReducerClass(CountReducer.class);
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
    FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
    FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
