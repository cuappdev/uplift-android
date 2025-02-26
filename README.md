# Uplift - Cornell Fitness

<p align="center"><img src=https://raw.githubusercontent.com/cuappdev/uplift-ios/master/Uplift/Assets.xcassets/AppIcon.appiconset/ItunesArtwork%402x.png width=210 /></p>

Uplift is one of the latest apps by [Cornell AppDev](http://cornellappdev.com), an engineering project team at Cornell University focused on mobile app development. Uplift aims to be the go-to fitness and wellness tool that provides information and class times for gym resources at Cornell.

## Getting Started
1. Clone the repository.
2. Add a file called secrets.properties into your root level directory with the variables which you can find pinned in `#uplift-android`.
3. Go to the Firebase console for Uplift (you may need to contact a lead for access), and navigate to Project Settings -> Android, then add your SHA1 fingerprint, which you can find with the following command `./gradlew signingReport`.
4. Download the google-services.json file which you can find pinned on `#uplift-android` and add it to the app directory.
5. Build the project, and you should be good to go!
