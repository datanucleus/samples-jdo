# attach-detach

Simple demonstration of the detach-attach process with JDO.

To run this, simply type "mvn clean test"


In this sample we have 3 classes. An _Owner_ has a 1-N relation with _Pet_. A _Pet_ has an _owner_ and a _Vet_.

We demonstrate detaching an object graph, including how to include fields in the fetch plan for detaching.
Then we demonstrate updating the object graph whilst detached, and reattaching it.
