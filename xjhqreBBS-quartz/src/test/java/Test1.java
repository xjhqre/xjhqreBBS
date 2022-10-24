import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.Test;

import com.xjhqre.common.utils.DateUtils;
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

    @Test
    void test2() {
        Date date = DateUtils.getNowDate();
        String month = new SimpleDateFormat("yyyy年MM月").format(date);
        Date date1 = DateUtils.parseDate(month);
        System.out.println(date);
    }
}
