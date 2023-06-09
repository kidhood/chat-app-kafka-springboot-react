import React, { useEffect, useState } from 'react'
import SockJS from 'sockjs-client'
import Stomp from 'stompjs';
import { getListUser } from '../api/UserApiService';
import { getMessagePrivate } from '../api/MessageApiService';
import { useDispatch } from 'react-redux';
import chatSlice from '../redux/chatSlice';

const MemberList = ({tab, setTab, username}) => {

    const [userList, setUserList] = useState([]);

    const dispatch = useDispatch()

    

    useEffect(() => {
        fresh()
      }, []);
    
    const fresh = () => {
      getListUser
      .then(response => {
        setUserList(response.data);
    
        const newPrivateChats = new Map();
        response.data.forEach((user) => {
          newPrivateChats.set(user.userName, []);
        });
    
        dispatch(chatSlice.actions.setPrivateChats(new Map(newPrivateChats)));
      })
      .catch(error => {
        // Handle error
        console.log(error);
      });
    }

    const handleChatPrivate = async (sender) => {
        await getMessagePrivate(username, sender)
          .then((response) => {

            // setPrivateChats((prevPrivateChats) => {
            // const updatedPrivateChats = new Map();
            // updatedPrivateChats.set(sender, response.data);
            //   return updatedPrivateChats;
            // });
              const updatedPrivateChats = new Map();
            updatedPrivateChats.set(sender, response.data);
            dispatch(chatSlice.actions.setPrivateChats(new Map(updatedPrivateChats)))

            console.log(response.data);
          })
          .catch((error) => console.log(error));
      };

  return (
    <div className="member-list">
    <ul>
        <li onClick={()=>{setTab("CHATROOM")}} className={`member ${tab==="CHATROOM" && "active"}`}>Chatroom</li>
        {userList.map((item)=>(
            username !== item.userName &&
                 (<li onClick={()=>{setTab(item.userName); handleChatPrivate(item.userName)}} className={`member ${tab===item.userName && "active"}`} key={item.id}>
                    {item.userName}
                </li>)
            
        ))}
    </ul>
</div>
  )
}

export default MemberList
