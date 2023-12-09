import com.heima.user.UserApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

/**
 * @projectName: heima-leadnews
 * @package: PACKAGE_NAME
 * @className: testSalt
 * @author: 丁海斌
 * @description: TODO
 * @date: 2023/12/7 15:52
 * @version: 1.0
 */
@SpringBootTest(classes = UserApplication.class)
@RunWith(SpringRunner.class)
public class testSalt {

    @Test
    public void test(){
        String pswd = DigestUtils.md5DigestAsHex(("123456" + "123").getBytes());
        System.out.println(pswd);
    }
}
