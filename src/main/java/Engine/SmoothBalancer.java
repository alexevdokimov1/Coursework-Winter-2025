package Engine;

public class SmoothBalancer {
    private final int latestVolumesCount = 5;
    private final float[] latestVolumes = new float[latestVolumesCount];

    public void addValue(float newValue){
        for(int i = latestVolumesCount-1; i>0; i--)
        {
            latestVolumes[i]=latestVolumes[i-1];
        }
        latestVolumes[0]=newValue;
    }

    public void printRow(){
        for(int i = 0; i<latestVolumesCount; i++)
            System.out.printf("%.0f\t", latestVolumes[i]);
        System.out.println();
    }

    public float getMax(){
        float max = latestVolumes[0];
        for(int i = 1; i<latestVolumesCount; i++)
            if(latestVolumes[i]>max) max = latestVolumes[i];
        return max;
    }

    public float getAvg(){
        float sum = 0.f;
        for(int i = 0; i<latestVolumesCount; i++)
           sum += latestVolumes[i];
        return (float)sum/latestVolumesCount;
    }
}
