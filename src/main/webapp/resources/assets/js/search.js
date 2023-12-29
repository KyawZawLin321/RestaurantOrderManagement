function performSearch() {
    const searchInput = $('#searchInput');
    const query = searchInput.val();
    console.log('ee',query)
	if(query!==null && query!=='' ){
		console.log('dd',query);

    // Clear previous search results
    $('#searchResultsContainer').empty();

    // Make an AJAX request to the backend
    $.ajax({
        url: 'http://localhost:8080/Restaurant_Order_Management/api/search',
       method: 'POST',
        data: { query },
        success: function (data) {
            displaySearchResults(data);
            console.log('aa',data);

        },
        error: function (error) {
            console.error('Error performing search:', error);
        }
    });
	}else{
   		 $('#searchResultsContainer').empty();
		
	}
	
	
}

function displaySearchResults(results) {
    const searchResultsContainer = $('#searchResultsContainer');
    if (!results && !results.restaurants && !results.foods && !results.foodSets) {
        searchResultsContainer.append('<li>No results found</li>');
    } else {
        if (results.restaurants.length === 0 && results.foods.length === 0 && results.foodSets.length === 0) {
            searchResultsContainer.append('<li>No results found</li>');
        } else {
            displayResultsForType(results.restaurants, 'Restaurants');
            displayResultsForType(results.foods, 'Food Items');
            displayResultsForType(results.foodSets, 'Food Set');
        }
    }
}


function displayResultsForType(results,type) {
    const searchResultsContainer = $('#searchResultsContainer');
				console.log('result1',results)
				console.log('type',type);
			console.log('restaurants',results.restaurants);
					console.log('food',results.foods);
					console.log('foodsets',results.foodSets);
					
       


    if (results.length > 0) {
			console.log('result length',results.length);

		if(type ==='Restaurants' ){
			 results.forEach(result => {
				 console.log('wqeqweqw');
            searchResultsContainer.append(`
            <a href="/Restaurant_Order_Management/user/menu?id=${result.id}"><p>${result.name }</p></a>`);
        });
		}else if(type ==='Food Items' ){
			 results.forEach(result => {
				 				 console.log('asdasdasd');

            searchResultsContainer.append(`
            <a href="/Restaurant_Order_Management/user/foodDetail/${result.id}"><p>${result.name }</p></a>`);
        });
			
		}else if(type ==='Food Set'){
			results.forEach(result => {
								 console.log('zxczXC');

            searchResultsContainer.append(`
            <a href="/Restaurant_Order_Management/user/foodSetDetail/${result.id}"><p>${result.name }</p></a>`);
		        });

       }
    }
}