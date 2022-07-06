# ComicReader

The project that available you to read&navigate through the comic on https://xkcd.com/.

![001](https://user-images.githubusercontent.com/4902107/177505154-daf78f0c-380d-4f4b-afce-0cf17c393da4.png)
![002](https://user-images.githubusercontent.com/4902107/177505182-535e43af-fe6a-4311-b449-ef67ceb400f8.png)

https://user-images.githubusercontent.com/4902107/177505202-d5dbea96-c184-4abe-89f5-987978b602bd.mp4

## 1st MVP feature
- Landing on latest comic available
- Navigate to next & previous comic.
- Random comic
- Search comic by number (Currently, there are issues on the xkcd search engine https://relevantxkcd.appspot.com as well, so we only able to provided search bu number untill it got fixes.)

## Implementation
* Pure kotlin
* Clean Architecture + MVVM & SOLID Principles as much as possisble.
* Currently have only unit test in significance class (ViewModel, Interactor, Repository)

## 3rd Party Libraries

Dagger2
RxJava,
Gson,
Glide,
Mockito,
Retrofit,
