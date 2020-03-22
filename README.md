# StarWarsKamino

The project is developed using the MVVM architecture and written in kotlin. 
Coroutines are used to trigger a HTTP GET call via the retrofit2 library, 
with the results shown on the UI using LiveData through a repository. 
Images are loaded using the Picasso library on demand.

Unit tests are developed for both repositories, 
as those are the classes that hold the most business logic of this simple app.

#### Possible Improvements

* Nicer UI
* In Resident List Fragment: Show resident names and pictures in the list. These are lazily loaded on demand, 
triggered when the recyclerview gets (close) to the bottom.
* Addition of Instrumentation/UI tests
