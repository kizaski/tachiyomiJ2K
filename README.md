| Download |
|-------|
| [![release](https://img.shields.io/github/release/getraid/tachiyomiJ2K-OCR.svg?maxAge=3600&label=download)](https://github.com/getraid/tachiyomiJ2K-OCR/releases)|


# ![app icon](./.github/readme-images/app-icon.png)TachiyomiJ2K OCR
TachiyomiJ2K OCR is a free and open source manga reader for Android 6.0 and above.  
This is a fork of the [J2K repository](https://github.com/Jays2Kings/tachiyomiJ2K), which also is based on the original [Tachiyomi project](https://github.com/tachiyomiorg/tachiyomi).  
This specific fork *borrowed* some code off the outdated [previous OCR Fork](https://github.com/Rattlehead15/tachiyomiOCR)

![screenshots of app](./.github/readme-images/screens.gif)


## About this Fork
I was looking into this app and found it quite nice. I'm not much of a manga reader myself, but thought this could be a superb opportunity to improve my japanese.  
...or at least so I thought. Japanese manga providers are a thing, but everything is either written exclusively in kanji or is tedious to translate *on the fly*.  
So I searched for a bit and found [this ocr repository](https://github.com/Rattlehead15/tachiyomiOCR).  
But it is outdated and very troublesome to merge, so I thought - f**k it, I'll just reimplement it again and hope I can manage to keep it updated enough, so that it doesn't break.  

I chose the preexisting J2K fork as a base, since the original version had many changes which were even worse to deal with.  
Plus the design is also much cleaner!

**Attention**: This requires AnkiDroid to be installed on your device. Later on I'll maybe make it optional.  

Current Status:  
- [x] Compilable
- [x] Gets the correct permissions
- [x] Gets into settings page
- [x] Shows the settings
- [x] Checks if Anki is installed
- [x] OCR Button is visible (needs check for white/dark mode)
- [x] OCR works
- [x] Word lookup works
- [ ] Settings fully working
- [ ] Anki fully integrated
- [ ] Redesign UI / interaction (annoying to access ocr)

**Overall Status**: ✔️ (Anki integration & Settings don't work yet)

Once a working status is achieved, a build will be released under the [releases page](https://github.com/getraid/tachiyomiJ2K-OCR/releases).
## Features

Features of Tachiyomi include:
* Online reading from a variety of sources
* Local reading of downloaded content
* A configurable reader with multiple viewers, reading directions and other settings.
* [MyAnimeList](https://myanimelist.net/), [AniList](https://anilist.co/), [Kitsu](https://kitsu.io/explore/anime), [Shikimori](https://shikimori.one), and [Manga Updates](https://www.mangaupdates.com/) support
* Categories to organize your library
* Automatic light and dark themes
* Schedule updating your library for new chapters
* Create backups locally to read offline or to your desired cloud service 

Plus some new features in this fork such as:
* New Manga details screens, themed by their manga covers
* Combine 2 pages while reading into a single one for a better tablet experience
* An expanded toolbar for easier one handed use (with the option to reduce the size back down)
* Floating searchbar to easily start a search in your library or while browsing
* Library redesigned as a single list view: See categories listed in a vertical view, that can be collasped or expanded with a tap
* Staggered Library grid
* Drag & Drop Sorting in Library
* Dynamic Categories: Group your library automatically by the tags, tracking status, source, and more
* New Recents page: Providing quick access to newly added manga, new chapters, and to continue where you left on in a series
* Stats Page
* New Themes
* Dynamic Shortcuts: open the latest chapter of what you were last reading right from your homescreen
* [New material snackbar](.github/readme-images/material%20snackbar.png): Removing manga now auto deletes chapters and has an undo button in case you change your mind
* Batch Auto-Source Migration (taken from [TachiyomiEH](https://github.com/NerdNumber9/TachiyomiEH))
* [Share sheets upgrade for Android 10](.github/readme-images/share%20menu.png)
* View all chapters right in the reader
* A lot more Material Design You additions
* Android 12 features such as automatic extension and app updates

## Issues, Feature Requests and Contributing

Please look into the originial J2K repository for everything that is not specifically targeted to this project.
For everything else: Feel free to contribute, post issues, etc...

## Feature Requests

If you have some general feature requests, again please head to the original J2k repo.
For specific improvements to this fork, lemme hear 'em! :)

## FAQ

[See our website.](https://tachiyomi.org/)
You can also reach out to us on [Discord](https://discord.gg/tachiyomi).

## License

    Copyright 2015 Javier Tomás

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

## Disclaimer

The developer of this application does not have any affiliation with the content providers available.
