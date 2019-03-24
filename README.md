This is a Rest API to recommend lodging to a guest.


With this API you can:
- Create guests;
- Add wishes places to a guest;
- See lodging recommendation to a guest.



## Context
Where someone go to a other city, usually this guy wanna visit some specific places. 
Image if someone can put in a system every place that he want to visit e see a list of hotel with the best position for he.
Well, this is what this API do.



## How it works?
First, you need to create a guest.
Here an example:


```
POST /guests
{
	"name": "Paulo"
}
```

Now, you need to add the guest wishes places.
Here an example:

```
POST guests/:guest_id/wishes
{
	"local_name" : "MIAU - Museu da Impresa Automativa",
	"latitude" : -23.529589,
	"longitude"  : -46.693559
}
```


To find the lodging, is created a latitude average and a longitude average of the all guest wishes place. After that, the google maps api is used to search the lodging list using this parameties(latitude average, longitude average) in a radius of 1500m.
You can see the lodging list using this end point:

```
 GET /guests/:guest_id/findHotel
```

## Here, some endpoints that can help you

Find all wishes from a guest:

```
GET /guests/:guest_id/wishes
```

Find all guests:

```
GET /guests
```

Find a guest by id:

```
GET /guests/:guest_id
```

### How run it

1. Create the databse `guest_wishes` in your postgres database.
2. Review `src\main\resources\application.properties` (you need to put your google maps api key here)
2. In your console, run `gradle bootrun`
