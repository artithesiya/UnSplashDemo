Android Unsplash Photo Viewer
This is a simple Android app that allows you to view photos from the Unsplash API in a grid layout. The app uses pagination to load more photos as you scroll and includes features for caching, handling network errors, and displaying a placeholder loader image.

Development Environment
Android Studio: Android Studio Iguana | 2023.2.1 Patch 2
Minimum API Level: API 21 (Android 5.0 Lollipop)
Target API Level: API 30 (Android 11)


API Details
Base URL: api.unsplash.com
Endpoint: /photos

Query Parameters:
per_page: 20 (Number of photos per page)

Features
Display photos in a grid layout using GridView.
Implement pagination for loading more photos as the user scrolls.
Cache images to disk for better performance and offline viewing.
Handle network errors gracefully with appropriate error messages.
Display a placeholder loader image while photos are being loaded.
Permissions
INTERNET: Required for accessing the Unsplash API.
WRITE_EXTERNAL_STORAGE / READ_EXTERNAL_STORAGE: Required for caching images to disk.

Usage
Make sure to replace YOUR_ACCESS_KEY_HERE with your actual Unsplash API access key.

Screenshots
[Insert screenshots here]
