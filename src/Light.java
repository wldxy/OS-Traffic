/**
 * Created by qq on 2016/5/2.
 */
public class Light {
    public Boolean isRed;
    public Boolean isYellow;
    public Integer x, y;
    public Integer width, height;

    public Light(Integer x, Integer y, Integer width, Integer height, Boolean isRed) {
        this.isRed = isRed;
        this.x = x;
        this.y = y;
        isYellow = false;
        this.width = width;
        this.height = height;
    }


}
