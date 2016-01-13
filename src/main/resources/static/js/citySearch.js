var citiesBloodhound = new Bloodhound({
    datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
    queryTokenizer: Bloodhound.tokenizers.whitespace,
    remote: {
        url: '/service/v1/city/?query=%QUERY',
        wildcard: '%QUERY'
    }
});

$('#cty.typeahead').typeahead(null, {
    name: 'city',
    display: 'name',
    source: citiesBloodhound,
    highlight: true,
    templates: {
        empty: "<div class='empty-message'>No matching cities found</div>",
        pending: "<div class='empty-message'>Looking...</div>",
        suggestion: function(city) {
            return '<div>' + city.name + ' (' + city.state.name + ')</div>'
        }
    }
});