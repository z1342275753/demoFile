public class demo {
    public static void main(String[] args) {
        int result=Calc.ADD.calc(1,2);
        System.out.println(result);
    }


    static enum Calc{
        ADD("+"){
            @Override
            public int calc(int a, int b) {
                return a+b;
            }
        },SUB("-"){
            @Override
            public int calc(int a, int b) {
                return a-b;
            }
        };
        private String calc;
        private Calc(String calc){
            this.calc=calc;
        }

//        public String getCalc() {
//            return calc;
//        }
        public abstract int calc(int a,int b);
    }
}
