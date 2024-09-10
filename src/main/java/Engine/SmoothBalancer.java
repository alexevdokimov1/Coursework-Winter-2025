package Engine;

public class SmoothBalancer {
    private final int latestVolumesCount;
    private final float[] latestVolumes;

    public SmoothBalancer(int slots){
        this.latestVolumesCount = slots;
        this.latestVolumes = new float[slots];
    }

    public SmoothBalancer(){
        this(5);
    }

    public void addValue(float newValue){
        for(int i = latestVolumesCount-1; i>0; i--)
        {
            latestVolumes[i]=latestVolumes[i-1];
        }
        latestVolumes[0]=newValue;
    }

    @Override
    public String toString() {
        StringBuilder outString = new StringBuilder();
        for(int i = 0; i<latestVolumesCount; i++)
            outString.append(String.format("%.0f\t", latestVolumes[i]));
        outString.append("\n");
        return outString.toString();
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
