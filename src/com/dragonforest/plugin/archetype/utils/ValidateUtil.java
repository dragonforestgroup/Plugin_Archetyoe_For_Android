package com.dragonforest.plugin.archetype.utils;

public class ValidateUtil {
    public static Result validePackageName(String packageName) {
        Result result = new Result();
        if (packageName == null || packageName.equals("")) {
            result.setOk(false);
            result.setMsg("packageName不能为空！");
            return result;
        }
        if (packageName.endsWith(".package")
                || packageName.contains(".package.")
                || packageName.contains("~")
                || packageName.contains("!")
                || packageName.contains("@")
                || packageName.contains("#")
                || packageName.contains("$")
                || packageName.contains("%")
                || packageName.contains("^")
                || packageName.contains("&")
                || packageName.contains("*")
                || packageName.contains("(")
                || packageName.contains(")")
                || packageName.contains("-")
                || packageName.contains("=")
                || packageName.contains("+")) {
            String msg = "pacakgeName can't contains charactor in {.package,~,@,#,$,%,^,&,*,(,),-，+，=}";
            result.setOk(false);
            result.setMsg(msg);
            return result;
        }
        return result;
    }

    public static class Result {
        boolean isOk = true;
        String msg = "validate pass";

        public boolean isOk() {
            return isOk;
        }

        public void setOk(boolean ok) {
            isOk = ok;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
