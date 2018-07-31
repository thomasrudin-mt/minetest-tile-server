
(function(tileserver){


    var MissionIcon = L.icon({
        iconUrl: 'pics/mission_32px.png',

        iconSize:     [32, 32],
        iconAnchor:   [16, 16],
        popupAnchor:  [0, -16]
    });

    function updateMission(mission) {
        var marker = L.marker([mission.z - 16, mission.x], {icon: MissionIcon});

        var popup = "<h4>" + mission.name + "</h4><hr>" +
          "<b>Description: </b> " + mission.description + "<br>" +
          "<b>Time: </b> " + mission.time + " seconds<br>" +
          "<b>Owner: </b> " + mission.owner + "<br>" +
          "<b>X: </b> " + mission.x + "<br>" +
          "<b>Y: </b> " + mission.y + "<br>" +
          "<b>Z: </b> " + mission.z + "<br>";

        marker.bindPopup(popup).addTo(tileserver.missionLayer);

    }

    function update(){
      m.request({ url: "missions" })
      .then(function(list){
        list.forEach(updateMission);
      });
    }

    //initial update
    update();

})(window.tileserver);