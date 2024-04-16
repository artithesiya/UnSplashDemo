<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>
<h1>Unsplash API Image Viewer</h1>
<p>This Android app fetches images from the Unsplash API and displays them in a grid view. It includes pagination for loading more images and implements a caching mechanism for efficient retrieval, not used in third party for image loading.</p>

<h2>Features</h2>
<ul>
<li>Fetches images from Unsplash API</li>
<li>Displays images in a grid view</li>
<li>Pagination support for loading more images</li>
<li>Disk catch for efficient retrieval</li>
<li>Error handling for network issues</li>
<li>Placeholder loader image</li>
</ul>

<h2>Setup</h2>
<ol>
<li>Clone the repository: <code><a href="https://github.com/artithesiya/UnSplashDemo.git">git clone https://github.com/artithesiya/UnSplashDemo.git</a> </code></li>
<li>Open the project in Android Studio (version Android Studio Iguana | 2023.2.1 Patch 2 or newer)</li>
<li>Build and run the app on a device or emulator</li>
</ol>

<h2>Permissions</h2>
<li>Required for network communication</li>
</br>
<code>"android.permission.ACCESS_NETWORK_STATE"
 "android.permission.INTERNET"
</code>
</br>
<li>Required for caching images from disk</li>
<h5>Devices running Android 12L (API level 32) or lower</h5>
<code>"android.permission.WRITE_EXTERNAL_STORAGE" 
"android:name="android.permission.READ_EXTERNAL_STORAGE"</code>
<h5>Devices running Android 13 (API level 33) or higher</h5>
<code>"android:name="android.permission.READ_MEDIA_IMAGES"</code>
<h5>To handle the reselection within the app on devices running Android 14
or higher if your app targets Android 14 (API level 34) or higher.</h5>
<code>"android:name="android.permission.READ_MEDIA_VISUAL_USER_SELECTED"</code>

<h2>Gradle</h2>
<li>Retrofit for API calling</li>
</br>
<code>implementation 'com.squareup.retrofit2:retrofit:2.9.0'</code></br>
<code>implementation 'com.squareup.retrofit2:converter-gson:2.9.0'</code>

<h2>API Information</h2>
<p><strong>BaseUrl:</strong> https://api.unsplash.com/</p>
<p><strong>EndPoint:</strong></p>
<ul>
<li><strong>collections/317099/photos</strong></li>
</ul>
<p><strong>Parameters:</strong></p>
<ul>
<li><strong>per_page:</strong> 15</li>
<li><strong>page:</strong> 1</li>
</ul>
<p><strong>Usage:</strong></p>
<ul>
<li><strong>Make sure to replace YOUR_ACCESS_KEY_HERE with your actual Unsplash API access key.</strong></li>
</ul>

</body>
</html>
