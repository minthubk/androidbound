package solutions.alterego.androidbound.parsers;

import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;

public class SelfParser implements IParser<String> {

    public final static IParser<String> instance = new SelfParser();

    @Override
    public void setLogger(IAndroidLogger logger) {
    }

    @Override
    public String parse(String content) {
        return content;
    }
}
