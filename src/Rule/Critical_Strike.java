package Rule;

import java.util.Random;

/**
 * 接入这个接口表示可以暴击的意思
 */
public interface Critical_Strike {
    default boolean Trigger(double probability){
        double factor = new Random().nextDouble();
        return factor<probability;
    }
}
