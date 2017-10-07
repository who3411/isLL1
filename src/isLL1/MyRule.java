package isLL1;

import java.util.HashMap;

class MyRule extends HashMap<String, Integer>{
    MyRule() {
        put("~", MyToken.EPSILON);
        put("::=", MyToken.EQUAL);
        put("(", MyToken.NLPAR);
        put(")", MyToken.NRPAR);
        put("[", MyToken.ALPAR);
        put("]", MyToken.ARPAR);
        put("{", MyToken.BLPAR);
        put("}", MyToken.BRPAR);
        put("|", MyToken.OR);
        put("\n", MyToken.NL);
    }
}
