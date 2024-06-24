window.$HandleBars.registerHelper('checkOutgoing', function (relation) {
    for(var i = 0; i < relation.length; i++){
        if(!relation[i].target._type.includes('Command') || !relation[i].target.examples){
            return true;
        }
    }
    return false;
});

window.$HandleBars.registerHelper('setExampleType', function (key, value, aggregateList, aggregate) {
    var type = 'String';
    if(aggregateList){
        for(var i = 0; i < aggregateList.length; i++){
            for(var j = 0; j< aggregateList[i].aggregateRoot.fieldDescriptors.length; j++){
                if(aggregateList[i].aggregateRoot.fieldDescriptors[j].name == key){
                    type = aggregateList[i].aggregateRoot.fieldDescriptors[j].className;
                }
            }
        }
    } else if(!aggregateList && aggregate){
        for(var i = 0; i < aggregate.aggregateRoot.fieldDescriptors.length; i++){
            if(aggregate.aggregateRoot.fieldDescriptors[i].name == key){
                type = aggregate.aggregateRoot.fieldDescriptors[i].className;
            }
        }
    }
    return type;
});

window.$HandleBars.registerHelper('checkExtendVerbType', function (type, path) {
    if(type == 'POST'){
        return path;
    } else {
        return '/1/'+ path;
    }
});