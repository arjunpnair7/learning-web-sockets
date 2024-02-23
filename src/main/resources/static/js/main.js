
var roomForm = document.querySelector('#room-form');
var roomPage = document.querySelector('#room-input')
var chatPage = document.querySelector('#chat-page');
var chatMembersList = document.querySelector('#chat-members');
var startVoteButton = document.querySelector('#start-vote');
var pokemonNames = document.querySelector('#pokemon-names');
var pokemonNames_list = document.querySelector('#pokemon-list');
var done_voting_button = document.querySelector('#submit');
var voteAgain_h1 = document.querySelector('#voteAgain');
var voteAgainButton = document.querySelector('#voteAgainButton');
var chooseRandomButton = document.querySelector('#chooseRandomButton');
var initialFiltering = true;
var loadingScreen;

var username = null;
var room_ident = null;
var stompClient = null;

console.log("line 7");

function onError(error) {
    console.log("LINE 10 - ERROR");
}

function connect(event) {
    console.log('test');

    room_ident = document.querySelector('#room-ident').value.trim();
    username = document.querySelector('#username').value.trim();

    if(room_ident) {
        roomPage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        console.log("Got client, now going to connect")
        stompClient.connect({}, onConnected, onError);

        console.log("finished connecting");
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    console.log("MESSAGE RECEIVED");
    var message = JSON.parse(payload.body);
    console.log("PAYLOAD: " + message.type);

    if (message.type == 'ADD_USER') {
//        console.log("LINE 44" + payload.body.userList);
        var joinedUsersList = message.userList;
        console.log("LENGTH: " + joinedUsersList.length);
        while (chatMembersList.firstChild) {
          chatMembersList.removeChild(chatMembersList.firstChild);
        }
        for (let i = 0; i < joinedUsersList.length; i++) {
            var element = document.createElement('li')
            element.textContent = joinedUsersList[i];
            chatMembersList.appendChild(element)
        }
    }
    console.log("LINE 56");
    if (message.type == 'DATA') {
        console.log("LINE 58");
        var pokemon_names = message.userList;
//        localStorage.setItem('data', JSON.stringify(pokemonNames));
        // Redirect to the new page
        console.log("LINE 62");

        console.log(pokemon_names);
        console.log(typeof(chatPage));
        console.log(typeof(pokemonNames));

        if (pokemonNames.classList === undefined) {
            console.log("UNDEFINED POKEMON NAME");
        } else {
            console.log("DEFINED POKEMON NAME")
        }
        pokemonNames.classList.remove('hidden');

        if (chatPage.classList === undefined) {
                    console.log("UNDEFINED CHATPAGE");
                } else {
                    console.log("DEFINED CHATPAGE");
                }
        chatPage.classList.add('hidden');
        pokemonNames.classList.remove('hidden');

        while (!initialFiltering && pokemonNames_list.firstChild) {
          pokemonNames_list.removeChild(pokemonNames_list.firstChild);
          console.log("REMOVING: LINE 93");
        }
        if (!initialFiltering) {
            startVoteButton.classList.remove('hidden');
            chooseRandomButton.classList.add('hidden');
            voteAgain_h1.classList.add('hidden');
            voteAgainButton.classList.add('hidden');
            done_voting_button.classList.remove('hidden');
            done_voting_button.disabled = false;
        }

        for (let i = 0; i < pokemon_names.length; i++) {
            var element = createPokemonListItem(pokemon_names[i])
            pokemonNames_list.appendChild(element)
        }
//        window.location.href = window.location.href + 'voting'
        console.log("LINE 64");
    }

    if (message.type == 'VOTE') {
        console.log("STILL VOTING");
    }
    if (message.type == 'DONE_VOTING') {
        console.log("LINE 103 - DONE VOTING");
        document.body.removeChild(loadingScreen);
//        loadingScreen.classList.add("hidden");
        console.log("DONE VOTING, CAN PROCEED");
        var filteredPokemonList = message.userList;

        while (pokemonNames_list.firstChild) {
          pokemonNames_list.removeChild(pokemonNames_list.firstChild);
        }
        for (let i = 0; i < filteredPokemonList.length; i++) {
            var element = document.createElement('li')
            element.textContent = filteredPokemonList[i];
            pokemonNames_list.appendChild(element)
        }
        done_voting_button.classList.add('hidden');
        initialFiltering = false;
        console.log("LINE 117");
        voteAgain_h1.classList.remove('hidden');
        voteAgainButton.classList.remove('hidden');
        chooseRandomButton.classList.remove('hidden');
        console.log("LINE 121");
    }

}

function createPokemonListItem(name) {
  // Create the list item
  var listItem = document.createElement('li');

  // Create the card
  var card = document.createElement('div');
  card.classList.add(); // Center vertically

  // Create the card body
  var cardBody = document.createElement('div');
  cardBody.classList.add('card-body');

  // Create the label
  var label = document.createElement('label');
  label.classList.add(); // Increase text size

  // Create the input element (checkbox)
  var checkbox = document.createElement('input');
  checkbox.type = 'checkbox';
  checkbox.name = 'pokemon';
  checkbox.value = name;
  checkbox.classList.add(); // Increase checkbox size

  // Add an event listener to the checkbox
  checkbox.addEventListener('click', function() {
    // Check if the checkbox is checked
    if (checkbox.checked) {
      // Get the value of the checkbox (the Pokémon name)
      var pokemonName = checkbox.value;

      // Send a message to the WebSocket
      // Replace this with your WebSocket code
      console.log('Sending message to WebSocket:', pokemonName);
      stompClient.send("/app/room/" + room_ident + "/vote", {}, username + ":" + pokemonName);
    }
  });

  // Add the input element to the label
  label.appendChild(checkbox);

  // Add the name to the label
  label.appendChild(document.createTextNode(name));

  // Add the label to the card body
  cardBody.appendChild(label);

  // Add the card body to the card
  card.appendChild(cardBody);

  // Add the card to the list item
  listItem.appendChild(card);

  return listItem;
}






function onConnected() {
    // Subscribe to the Public Topic
    console.log("LINE 27");
    stompClient.subscribe('/room/' + room_ident, onMessageReceived);

    // Tell your username to the server
    stompClient.send("/app/room/" + room_ident + "/add",
        {}, username)

    console.log("LINE 33");
}

voteAgainButton.addEventListener('click', function() {
    console.log('Vote again clicked!');
//    startVoteButton.classList.remove('hidden');
//    chooseRandomButton.classList.add('hidden');
//    voteAgain_h1.classList.add('hidden');
//    while (pokemonNames_list.firstChild) {
//        pokemonNames_list.removeChild(pokemonNames_list.firstChild);
//    }
    stompClient.send("/app/room/" + room_ident + "/start", {}, "Voting.html");
//    voteAgainButton.classList.add('hidden');
//    done_voting_button.classList.remove('hidden');
//    done_voting_button.disabled = false;
})

startVoteButton.addEventListener('click', function() {
     console.log('Button clicked!');
     stompClient.send("/app/room/" + room_ident + "/start", {}, "Voting.html");
   });


roomForm.addEventListener('submit', connect, true)

done_voting_button.addEventListener('click', function() {
  // Your event handler code here
  stompClient.send("/app/room/" + room_ident + "/donevoting", {}, username);
  console.log('Submit button clicked!');

    done_voting_button.disabled = true;
    loadingScreen = document.createElement('div');
    loadingScreen.className = 'loading-screen';
    loadingScreen.innerHTML = '<div class="spinner"></div><p>Waiting for your group to finish voting...</p>';
    document.body.appendChild(loadingScreen);
});

