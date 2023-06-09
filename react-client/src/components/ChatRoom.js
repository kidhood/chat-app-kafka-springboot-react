import React, { useEffect, useState } from 'react'
import {over} from 'stompjs';
import SockJS from 'sockjs-client';
import { registerNewUser, sendMessagePrivate, sendPublicMessage } from '../api/MessageApiService';
import MemberList from './MemberList';
import { useDispatch, useSelector } from 'react-redux';
import chatSlice, { getPrivateChats } from '../redux/chatSlice';
import { enableMapSet } from 'immer';


var stompClient =null;
const ChatRoom = () => {
    // const [privateChats, setPrivateChats] = useState(new Map());     
    const [publicChats, setPublicChats] = useState([]); 

    const [tab,setTab] =useState("CHATROOM");

    const privateChats = useSelector(getPrivateChats)

    const dispatch = useDispatch();

    enableMapSet();

    const [userData, setUserData] = useState({
        username: '',
        receivername: '',
        connected: false,
        message: ''
      });
    useEffect(() => {
      console.log(userData);
    }, [userData]);

    const connect =()=>{
        let Sock = new SockJS('http://localhost:8080/ws');
        stompClient = over(Sock);
        stompClient.connect({},onConnected, onError);
    }

    const onConnected = () => {
        setUserData({...userData,"connected": true});
        stompClient.subscribe('/chatroom/public', onMessageReceived);
        stompClient.subscribe('/user/'+userData.username+'/private', onPrivateMessage(privateChats));
        userJoin();
    }

    const userJoin=()=>{
          var chatMessage = {
            senderName: userData.username,
            status:"JOIN"
          };
          stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
    }

    const onMessageReceived = (payload)=>{
        var payloadData = JSON.parse(payload.body);
        switch(payloadData.status){
            case "JOIN":
                if(!privateChats.get(payloadData.senderName)){
                    privateChats.set(payloadData.senderName,[]);
                    // setPrivateChats(new Map(privateChats));
                    // dispatch(chatSlice.actions.setPrivateChats(new Map(privateChats)))
                    dispatch(chatSlice.actions.addNewMessage({key:payloadData.senderName, value: payloadData}))
                }
                break;
            case "MESSAGE":
                publicChats.push(payloadData);
                setPublicChats([...publicChats]);
                break;
        }
    }
    
    console.log(privateChats)

    const onPrivateMessage = (privateChats) => {
      return (payload) => {
        const payloadData = JSON.parse(payload.body);
        const senderName = payloadData.senderName;
        console.log(privateChats)
        
        // if (privateChats.get(senderName)) {
        //   const chatList = [...privateChats.get(senderName)];
        //   chatList.push(payloadData);
        //   privateChats.set(senderName, chatList);
        //   dispatch(chatSlice.actions.addNewMessage({key:senderName, value: payloadData.message}))
        // } else {
        //   // privateChats.set(senderName, [payloadData]);
        //   dispatch(chatSlice.actions.addNewMessage({key:senderName, value: payloadData.message}))
        // }
        dispatch(chatSlice.actions.addNewMessage({key:senderName, value: payloadData}))
      }
    };

    const onError = (err) => {
        console.log(err);
        
    }

    const handleMessage =(event)=>{
        const {value}=event.target;
        setUserData({...userData,"message": value});
    }
    const sendValue=()=>{
            if (stompClient) {
              var chatMessage = {
                senderName: userData.username,
                message: userData.message,
                status:"MESSAGE"
              };
              console.log(chatMessage);
            //   stompClient.send("/app/message", {}, JSON.stringify(chatMessage));
              try{
                const reponse = sendPublicMessage(chatMessage)
                console.log(reponse.data)
            }catch(error){
    
            }
              setUserData({...userData,"message": ""});
            }
    }

    const sendPrivateValue=()=>{
        if (stompClient) {
          var chatMessage = {
            senderName: userData.username,
            receiverName:tab,
            message: userData.message,
            status:"MESSAGE"
          };
          
        //   if(userData.username !== tab){
        //     privateChats.get(tab).push(chatMessage);
        //     setPrivateChats(new Map(privateChats));
        //   }
        if (userData.username !== tab) {
          const updatedPrivateChats = new Map(privateChats);
          const chatsForTab = [...(updatedPrivateChats.get(tab) || [])]; // Create a copy of the array
          chatsForTab.push(chatMessage);
          updatedPrivateChats.set(tab, chatsForTab);
          dispatch(chatSlice.actions.setPrivateChats(updatedPrivateChats));
        }
        //   stompClient.send("/app/private-message", {}, JSON.stringify(chatMessage));
        try{
            const reponse = sendMessagePrivate(chatMessage)
            console.log(reponse.data)
        }catch(error){

        }
        setUserData({...userData,"message": ""});
        }
    }

    const handleUsername=(event)=>{
        const {value}=event.target;
        setUserData({...userData,"username": value});
    }

    const registerUser= async ()=>{
        connect();
        await registerNewUser(userData.username)
        .then(response => console.log(response))
        .catch(error => console.log(error))
    }

    console.log(privateChats.get(tab))

    return (
    <div className="container">
        {userData.connected?
        <div className='container-chat-box'>
            <h1 className="user-name">{userData.username}</h1>
            <div className="chat-box">

            <MemberList  tab={tab} setTab={setTab} username={userData.username}/>

            {tab==="CHATROOM" && <div className="chat-content">
                <ul className="chat-messages">
                    {publicChats.map((chat,index)=>(
                        <li className={`message ${chat.senderName === userData.username && "self"}`} key={index}>
                            {chat.senderName !== userData.username && <div className="avatar">{chat.senderName}</div>}
                            <div className="message-data">{chat.message}</div>
                            {chat.senderName === userData.username && <div className="avatar self">{chat.senderName}</div>}
                        </li>
                    ))}
                </ul>

                <div className="send-message">
                    <input type="text" className="input-message" placeholder="enter the message" value={userData.message} onChange={handleMessage} /> 
                    <button type="button" className="send-button" onClick={sendValue}>send</button>
                </div>
            </div>}
            {tab!=="CHATROOM" && <div className="chat-content">
                 <ul className="chat-messages">
                    {(privateChats.get(tab) || []).map((chat, index) => (
                        <li className={`message ${chat.senderName === userData.username && "self"}`} key={index}>
                        {chat.senderName !== userData.username && <div className="avatar">{chat.senderName}</div>}
                        <div className="message-data">{chat.message}</div>
                        {chat.senderName === userData.username && <div className="avatar self">{chat.senderName}</div>}
                        </li>
                    ))}
                </ul>

                <div className="send-message">
                    <input type="text" className="input-message" placeholder="enter the message" value={userData.message} onChange={handleMessage} /> 
                    <button type="button" className="send-button" onClick={sendPrivateValue}>send</button>
                </div>
            </div>}
        </div>
        </div>
        :
        <div className="register">
            <input
                id="user-name"
                placeholder="Enter your name"
                name="userName"
                value={userData.username}
                onChange={handleUsername}
                margin="normal"
              />
              <button type="button" onClick={registerUser}>
                    connect
              </button> 
        </div>}
    </div>
    )
}

export default ChatRoom
