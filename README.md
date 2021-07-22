
<br>
<br>

<p align="center">
    <img src="./art/banner.png">
</p>

<br>
<br>

**FlyNow** is a `CI` - `gradle Plugin` that does :
-  Generates an `APK` with the name of `Month-Day_Hour-GitBranch.apk`
- Authenticates  your `Google Drive Account` with Your [Google Drive API Application](https://developers.google.com/drive/api/v3/enable-drive-api) for you.
- Uploads the `APK`  into Your own `Google Drive`
- Notify `Slack Channel` with your Last `git Commit message` by default

-  Or if configured with `useCustomMessage = true` you will need to supply a `-Pmessage` message through the command line `./gradlew flyNow -Pmessage="Your custom Slack message contnet"`


## Add the plugin
#### build.gradle.kts (:app)
```
plugins {
    ...
    id("com.esmaeel.flyNow") version "1.0.0"
}

```
## OR
#### build.gradle (:app)
```
plugins {  
  ...
  id 'com.esmaeel.flyNow'  version  '1.0.0'
}
```


## Configure the plugin

```kotlin
flyNow {  

  useCustomMessage = true
    
  // slack channel webhook  
  channelWebhook="https://hooks.slack.com/services/T02FREEE2N/BNCH/5r6HWbFaWE"  
  
  //Drive credentials  
  clientId = "991323803847-458679708868756878675468790089.apps.googleusercontent.com"  
  clientSecret = "UJhxEll4n4dfGD2GJ"  
  
  authorName = "<@U01JL0P2LCU>"  
  botName = "Release Bot"  
  
}
```

> **ProTip:** You can make  **useCustomMessage = flase** or remove it in the **flyNow Config above** and the slack message will contain the **Last Git Commit Message 🥶**.



## How to use.

- `./gradlew flyNow` OR `./gradlew flyNow -Pmessage="Custom changes Message for slack message"`

<p align="start">
    <img src="https://github.com/EsmaeelNabil/FlyNow/blob/master/art/usage.png">
</p>

#### For the first time the commandLine will ask you to open a `URL` to authenticate your `Drive Application api` with your `Google Drive` 
- So you should open and authorize, to continue and `Upload`

<p align="start">
    <img src="https://github.com/EsmaeelNabil/FlyNow/blob/master/art/logs.png">
</p>

#### Congrats!!

<p align="start">
    <img src="https://github.com/EsmaeelNabil/FlyNow/blob/master/art/slackmessage.png" height="100">
</p>
