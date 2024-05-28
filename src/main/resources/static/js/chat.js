const chatInput = document.getElementById('message')
const chatHistory = document.getElementById('chat-history')
const loading = document.getElementById('loading')
let currentChat = null

let stompClient = null

function connect() {
    const socket = new SockJS('/chat-socket');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConnect, onError);
}

function onConnect() {
    stompClient.subscribe('/topic/public', mesReceived);
    /*
    const chats = getChatList()

    for (let chat in chats) {
        stompClient.subscribe(`/topic/${chat.id}`, mesReceived);
    }
    */
    stompClient.send(
        "/app/mes.addUser",
        {},
        JSON.stringify({})
    )
    loading.style.display = 'none'
    currentChat = 'public'
}

function onError() {
    loading.style.display = 'block'
    loading.textContent = 'Could not load chat. Pls try again!'
    loading.style.color = 'red'
}

function getChatList() {
    let chats = null
    fetch(url + `/api/chat`, {
        method: 'GET',
    })
        .then(res => res.json())
        .then(data => {
            console.log(data)
            chats = data
        })
        .catch(err => console.log(err))

    return chats
}

async function getChat(chat) {
    await fetch(url + `/api/chat/${chat}`, {
        method: 'GET',
    })
        .then(res => res.json())
        .then(data => {
            loadChat(data)
            console.log(data)
        })
        .catch(err => console.log(err))
}

async function addChat() {
    const newChat = null
    await fetch(url + `/api/chat`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            newChat
        }),
    })
        .then(res => res.json())
        .then(data => {
            loadChat(data)
            console.log(data)
        })
        .catch(err => console.log(err))
}

function sendMessage() {
    if (currentChat === null) return alert('No Chat is currently selected!')
    const chat = currentChat
    const mes = chatInput.value.trim()

    if(chat && mes){
        const chatMes = {
            message: mes,
            chat: chat
        }

        stompClient.send("/app/mes.send", {}, JSON.stringify(chatMes))
        chatInput.value = ''
    }
}

async function addUser() {
    const email = null
    await fetch(url + `/api/auth/login`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            email
        }),
    })
        .then(res => res.json())
        .then(data => {
            loadChat(data)
            console.log(data)
        })
        .catch(err => console.log(err))
}

function loadChat(chatRoom){
    activateChat(chatRoom)

}

function activateChat(chatRoom) {
    loading.textContent = "Loading chat..."
    loading.style.display = "block"

    const rooms = document.getElementsByTagName('chat-room')

    for(let i = 0; i<rooms.length; i++){
        rooms[i].classList.add('text-white')
        rooms[i].classList.remove('active')
    }

    document.getElementById(chatRoom).classList.remove('text-white')
    document.getElementById(chatRoom).classList.add('active')
    currentChat = chatRoom
}

function addChatToList(chat){
    const li = document.createElement('li')
    li.classList.add('nav-item');
    li.innerHTML = `<a href="#" id="${chat.id}" class="nav-link text-white chat-room" aria-current="page" onclick="loadChat('${chat.id}')">
                        <svg class="bi me-2" width="16" height="16"></svg>
                        chat.name
                    </a>`

    document.getElementById('chat-list').innerHTML += li
}

function mesReceived(payload){
    const mes = JSON.parse(payload.body)
    addToChat(mes)
}

function addToChat(mes){
    const div = document.createElement('div');
    div.classList.add('message')

    const subDiv = document.createElement('div');
    subDiv.innerHTML = mes.message;

    if (mes.type == 'USER'){
        div.classList.add('incoming')
        div.innerHTML = `<div><b>${mes.user_id}</b></div>`
    }else{
        div.classList.add('server')
    }

    div.innerHTML += subDiv
    chatHistory.innerHTML += div;
}
