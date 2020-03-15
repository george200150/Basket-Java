import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MainTest {
    private Main main;
    @Before
    public void setUp() throws Exception {
        main = new Main();
    }

    @After
    public void tearDown() throws Exception {
        //garbage collector
    }

    @Test
    public void maxTest() {

        assert main.max(1,1) == 1;
        assert main.max(1,2) == 2;
        assert main.max(2,3) == 3;
        assert main.max(-2,3) == 3;
        assert main.max(2,-3) == 2;

    }

    @Test
    public void mainTest() {

        Main.main(null);

    }


}
