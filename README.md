# Dgcrypt

Dgcrypt - Android is a simple Android library for encryption and decryption using AES-256-CBC. It supports both Kotlin and Java, and is designed to securely decrypt data that was encrypted on a backend, such as a PHP server using the Dgcrypt PHP library.

## Platform Compatibility
The Dgcrypt library is designed to work seamlessly across multiple platforms. You can find corresponding libraries for the following platforms:

- **PHP**: [Dgcrypt-PHP](https://github.com/davodm/dgcrypt-php)
- **Node.js**: [Dgcrypt-Node](https://github.com/davodm/dgcrypt-node)

These libraries allow you to easily decrypt data that was encrypted on the backend using this PHP library, ensuring secure communication between your backend and client applications.

## Installation

Add the library to your project:

1. Add the JitPack repository to your build file. Add it in your root `build.gradle` at the end of repositories:

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

2. Add the dependency:
```gradle
dependencies {
    implementation 'com.github.davodm:dgcrypt-android:1.0.0'
}
```


## Usage
### Kotlin
#### Encrypting Data:

```kotlin
val key = "your-32-character-long-key"
val iv = "your-16-character-iv"
val plainText = "Hello, World!"

try {
    val encrypted = Dgcrypt.encrypt(plainText, key, iv)
    println("Encrypted: $encrypted")
} catch (e: Exception) {
    e.printStackTrace()
}
```

#### Decrypting Data:
```kotlin
val key = "your-32-character-long-key"
val iv = "your-16-character-iv"
val encryptedText = "your-encrypted-text"

try {
    val decrypted = Dgcrypt.decrypt(encryptedText, key, iv)
    println("Decrypted: $decrypted")
} catch (e: Exception) {
    e.printStackTrace()
}
```

### Java
#### Encrypting Data:

```java
String key = "your-32-character-long-key";
String iv = "your-16-character-iv";
String plainText = "Hello, World!";

try {
    String encrypted = Dgcrypt.encrypt(plainText, key, iv);
    System.out.println("Encrypted: " + encrypted);
} catch (Exception e) {
    e.printStackTrace();
}
```

#### Decrypting Data:
```java
String key = "your-32-character-long-key";
String iv = "your-16-character-iv";
String encryptedText = "your-encrypted-text";

try {
    String decrypted = Dgcrypt.decrypt(encryptedText, key, iv);
    System.out.println("Decrypted: " + decrypted);
} catch (Exception e) {
    e.printStackTrace();
}
```

## Best Practices for Securing the Master Key
To further enhance the security of the master key, consider the following best practices:

1. **Use Firebase Remote Config**: Store the master key in Firebase Remote Config and fetch it at runtime. This way, you can easily update the master key without having to release a new version of your app.
```java
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class RemoteConfigHelper {
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    
    public RemoteConfigHelper() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
    }
    
    public void fetchAndActivate() {
        mFirebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String masterKey = mFirebaseRemoteConfig.getString("master_key");
                // Use the master key
            } else {
                // Handle the error
            }
        });
    }
}
```

2. **SharedPreferences**: Securely Store Secrets Using Encrypted SharedPreferences
You can use EncryptedSharedPreferences to securely store the encrypted master key locally.

```java
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;
import android.content.Context;
import android.content.SharedPreferences;

public class SecureStorageHelper {
    private SharedPreferences encryptedSharedPreferences;

    public SecureStorageHelper(Context context) throws Exception {
        String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        encryptedSharedPreferences = EncryptedSharedPreferences.create(
            "secure_prefs",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }

    public void storeEncryptedKey(String encryptedKey) {
        SharedPreferences.Editor editor = encryptedSharedPreferences.edit();
        editor.putString("encrypted_master_key", encryptedKey);
        editor.apply();
    }

    public String getEncryptedKey() {
        return encryptedSharedPreferences.getString("encrypted_master_key", null);
    }
}
```

## License
This project is licensed under the MIT License - see the [LICENSE](./LICENSE) file for details.

## Author
Davod Mozafari - [Twitter](https://twitter.com/davodmozafari)

