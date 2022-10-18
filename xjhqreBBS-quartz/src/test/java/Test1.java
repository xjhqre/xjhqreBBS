import org.junit.jupiter.api.Test;

import com.xjhqre.common.utils.StringUtils;

/**
 * <p>
 * Test1
 * </p>
 *
 * @author xjhqre
 * @since 10月 18, 2022
 */
public class Test1 {

    @Test
    public void name() {
        String methodStr = StringUtils.substringBetween("ryTask.ryParams('ry')", "(", ")");
        System.out.println(methodStr);
    }
}
