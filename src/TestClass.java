public class TestClass {

    @BeforeSuite
    void beforeTests() {
        System.out.println("beforeTests");
    }

//    @BeforeSuite
//    void beforeTests1() {
//        System.out.println("beforeTests");
//    }

    @Test(priority = 2)
    void test1() {
        System.out.println("test1 (priority = 2)");
    }

    @Test(priority = 1)
    void test2() {
        System.out.println("test2 (priority = 1)");
    }

    @AfterSuite
    void afterTests() {
        System.out.println("afterTests");
    }
}
