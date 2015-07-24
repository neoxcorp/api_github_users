# api_github_users

Android application, which allows to get list of github users loaded into ListView. Each row contains login, profile link (html_url) and avatar preview(100x100).
Clicking on avatar preview should result in opening bigger avatar version (about 400x400)

We expect application will:

- Allow to browse at least first 300 users (& more, lazy loading is done).
- Implement some kind of image caching.
- Show some kind of progress indicator if user waits for background task to complete, but do not block user interaction with application.
- Allow Activity recreation during configuration change (like orientation change).
