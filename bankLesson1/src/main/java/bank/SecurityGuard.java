package bank;

import org.apache.log4j.Logger;

public class SecurityGuard{
    
    private static final Logger log = Logger.getLogger(SecurityGuard.class);
    
    public SecurityGuard() {
        log.info(talk());
    }

    private String talk() {
        return "Привет, ты находишься в банке \" МегаБанк\", "
                + "если хочешь зарегистрироваться, пройди к менеджеру";
    }

}
