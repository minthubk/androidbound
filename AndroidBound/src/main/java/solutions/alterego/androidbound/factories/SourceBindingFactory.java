package solutions.alterego.androidbound.factories;

import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;

import java.util.Arrays;
import java.util.List;

import solutions.alterego.androidbound.binds.ChainedBinding;
import solutions.alterego.androidbound.binds.CommandBinding;
import solutions.alterego.androidbound.binds.PropertyBinding;
import solutions.alterego.androidbound.binds.SelfBinding;
import solutions.alterego.androidbound.interfaces.IBinding;
import solutions.alterego.androidbound.interfaces.IBindingFactory;

public class SourceBindingFactory implements IBindingFactory {

    protected IAndroidLogger mLogger;

    public SourceBindingFactory(IAndroidLogger logger) {
        setLogger(logger);
    }

    public IBinding create(Object source, String combinedPath, boolean needChangesIfPossible) {
        String[] tokens = combinedPath.split("\\.");
        return create(source, Arrays.asList(tokens), needChangesIfPossible);
    }

    public IBinding create(Object source, List<String> pathTokens, boolean needChangesIfPossible) {
        if (pathTokens == null || pathTokens.size() == 0) {
            throw new UnsupportedOperationException("Empty token list passed to binding factory");
        }

        String current = pathTokens.get(0);
        if (pathTokens.size() == 1) {
            return createLeaf(source, current, needChangesIfPossible);
        }
        return createChained(source, current, pathTokens.subList(1, pathTokens.size()), needChangesIfPossible);
    }

    protected IBinding createLeaf(Object source, String property, boolean needChangesIfPossible) {
        if (property.equals("this")) {
            return new SelfBinding(source, mLogger);
        } else if (CommandBinding.isCommand(source, property)) {
            return new CommandBinding(source, property, mLogger);
        } else {
            return new PropertyBinding(source, property, needChangesIfPossible, mLogger);
        }
    }

    protected IBinding createChained(Object source, String property, List<String> remainingTokens, boolean needChangesIfPossible) {
        return new ChainedBinding(source, property, remainingTokens, needChangesIfPossible, this, mLogger);
    }

    public void setLogger(IAndroidLogger logger) {
        mLogger = logger.getLogger(this);
    }
}
