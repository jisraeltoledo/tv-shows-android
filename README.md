# tv-shows-android

◦	Main architecture decisions you've made and a quick explanation of why.

I used 2 different views: 1 for the listings and the second one for the detail view.

I Used an API service in order to consume the API. And used Sharedpreferences to store the favorites. 
My first thaught was to use a local database, but, it was a lot of work for a very simple task, that's why I 
chose the shared preferences. Since the app doesn't requires any SQL functoinality, it works fine.

◦	List of third party libraries and why you chose each one.

#Glide 

For loading images.

#swipeactionadapter

For handling the swipe event on the rows of the list view.

# gson

For serializing and deserializing objects stored on shared preferences

# Retrofit

For consuming the rest API

◦	What in your code could be improved if you had more time.

I probably use a local database instead of shared preferences, and change the background color on the swipe event.
Also the app doesn't update the listing until the user changes from favorites to shows and viceversa.

◦	Mention anything that was asked but not delivered and why, and any additional comments.

1. Actions background color
2. Confirmation dialog to delete from favorites
3. Retry dialog in case action's fails.


I think, everything works as expected.



