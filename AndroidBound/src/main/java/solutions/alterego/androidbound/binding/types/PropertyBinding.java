package solutions.alterego.androidbound.binding.types;

import com.alterego.advancedandroidlogger.interfaces.IAndroidLogger;

import rx.Subscription;
import solutions.alterego.androidbound.helpers.Reflector;
import solutions.alterego.androidbound.helpers.reflector.PropertyInfo;
import solutions.alterego.androidbound.binding.interfaces.INotifyPropertyChanged;

public class PropertyBinding extends BindingBase {

    private Subscription mMemberSubscription;

    private PropertyInfo mPropertyInfo;

    public PropertyBinding(Object subject, String propertyName, boolean needChangesIfPossible, IAndroidLogger logger) {
        super(subject, logger);

        mPropertyInfo = Reflector.getProperty(subject.getClass(), propertyName);
        setupBinding(subject, mPropertyInfo.getPropertyName(), needChangesIfPossible);
    }

    private void setupBinding(Object subject, final String propertyName, boolean needChangesIfPossible) {
        if (subject == null) {
            return;
        }

        if (needChangesIfPossible && (getSubject() instanceof INotifyPropertyChanged)) {
            setupChanges(true);
            getLogger().debug(propertyName + " implements INotifyPropertyChanged. Subscribing...");

            mMemberSubscription = ((INotifyPropertyChanged) subject).onPropertyChanged()
                    .filter(member -> member.equals(propertyName))
                    .subscribe(s -> {
                        onBoundPropertyChanged();
                    });
        } else {
            setupChanges(false);
        }
    }

    protected PropertyInfo getInfo() {
        return mPropertyInfo;
    }

    protected void onBoundPropertyChanged() {
        notifyChange(getValue());
    }

    @Override
    public Class<?> getType() {
        return mPropertyInfo.getPropertyType();
    }

    @Override
    public Object getValue() {
        if (mPropertyInfo.isCanRead()) {
            return mPropertyInfo.getValue(getSubject());
        }

        getLogger().warning("Cannot get value for property " + mPropertyInfo.getPropertyName() + ": property is non-existent");
        return noValue;
    }

    @Override
    public void setValue(Object value) {
        if (mPropertyInfo.isCanWrite()) {
            mPropertyInfo.setValue(getSubject(), value);
        } else {
            if (mPropertyInfo.isCanRead()) {
                getLogger().warning("Cannot set value for property " + mPropertyInfo.getPropertyName() + ": propery is read-only");
            } else {
                getLogger().warning("Cannot set value for property " + mPropertyInfo.getPropertyName() + ": propery is non-existent");
            }
        }
    }

    @Override
    public void dispose() {
        if (mMemberSubscription != null) {
            mMemberSubscription.unsubscribe();
            mMemberSubscription = null;
        }
        super.dispose();
    }
}
