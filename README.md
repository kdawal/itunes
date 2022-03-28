
<div align="center">

<h3 align="center">iTunes Search App</h3>

  <p align="center">
    App that allows user to search for content within the iTunes Store, App Store, iBooks Store and Mac App Store.
</div>


<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>


## About The Project

There are a lot of design pattern and architecture you can follow, however,
The MVVM clean architecture as suggested by Google with the implementation of JetPack library can be the best architecture to follow for every project. A lot of resources can be found in Google. For starter, Google also provided tutorial in there Codelab.


Here's Why:
* It is easy to follow.
* Can fully utilize the creation of interfaces for your project design.
* Reduces business logic required in the views.
* Easy for your unit testing
* Separation of concerns
* Project strucure is even easier to navigate

[![App Screen Shot][app-screenshot1]] 

<p align="right">(<a href="#top">back to top</a>)</p>

### Built With

* [Android JetPack](https://developer.android.com/jetpack?gclid=Cj0KCQjw8_qRBhCXARIsAE2AtRYwLZxdicYKpP6txPxDIMNRNBMkmmgtYhBoLsscjd6TRKDrnCB5Yl0aAsojEALw_wcB&gclsrc=aw.ds)
* [Retrofit](https://square.github.io/retrofit/)
* [RxJava](https://github.com/ReactiveX/RxJava)
* [OkHttp](https://square.github.io/okhttp/)
* [Glide](https://github.com/bumptech/glide)
* [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)

<p align="right">(<a href="#top">back to top</a>)</p>
<!-- GETTING STARTED -->
## Getting Started
First, we need to test our URL's if those endpoints are actually working. 

### Prerequisites

These are the tools that I used for this projects
* Android studio - Integrated Development Environment (IDE)
* Postman - Use to test endpoints
* JSON to Kotlin Class - Android studio plugin for model creation

### Setup

1. Get started by following these [Documentation](https://developer.apple.com/library/archive/documentation/AudioVideo/Conceptual/iTuneSearchAPI/Searching.html#//apple_ref/doc/uid/TP40017632-CH5-SW1)_

2. Test endpoints using postman. Example: 
> https://itunes.apple.com/search?term=star&amp:country=au&amp:media=movie&amp:all

[![Postman Screen Shot][postman-screenshot]]

3. Change your base url in `gradle.properties` (optional)
4. Under `data/api/TrackAPIService` you can change your default `term`

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- USAGE -->
## Usage

By default, the app contains the list of tracks from `term=star` from the iTunes Search API.
You can also search, add favorite and remove if you want. By selecting item from the list,
will be able to view more information of the selected item.


<p align="right">(<a href="#top">back to top</a>)</p>


<!-- CONTACT -->
## Contact
Developer email -  kjdawal@gmail.com

[Facebook](https://www.facebook.com/kjdawal/)

[LinkeIn](https://www.linkedin.com/in/kim-joseph-dawal-570a20120/)


<p align="right">(<a href="#top">back to top</a>)</p>


<!-- MARKDOWN LINKS & IMAGES -->
[app-screenshot1]: images/app_screenshot1.jpg
[postman-screenshot]: images/postman.jpg