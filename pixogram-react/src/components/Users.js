import React, { Component } from 'react'
import {
    Link
} from "react-router-dom";
import { connect } from 'react-redux';
import * as actions from '../actions/action';
import { Redirect } from 'react-router-dom';

class Users extends Component {
    constructor(props) {
        super(props)

        this.state = {
            users: []
        }
    }
    componentDidMount() {
        console.log('Initialization code goes here..');
        this.props.onFetchUsers();
        // fetch('http://localhost:8080/api/userall')
        //     .then(response => response.json())
        //     .then(data => {
        //         console.log(data)
        //         this.setState({ users: data })
        //     });
    }
    // addUser(props){
    //     this.props.history.push('http://localhost:8080/api/useradd');
    // }

    delete(user_id) {
        console.log('delete employee with id: ' + user_id)
        this.props.onDeleteUser(user_id);
        // if (window.confirm('Are you sure?')) {

        //     fetch("http://localhost:8080/api/userdel/" + user_id, {
        //         method: 'DELETE',
        //         headers: {
        //             'Accept': 'application/json',
        //             'Content-Type': 'application/json'
        //         }
        //     }).then(() => {
        //         console.log('removed');
        //     }).catch(err => {
        //         console.error(err)
        //     });
        // }
    }
    // updateUser() {
    //     var url = "http://localhost:8080/api/userupd";
    //     var data = {
    //         user_id: 24 , 
    //         username: 'example123', 
    //         password: 'Pass@123', 
    //         email: 'example@gmail.com', 
    //         gender: 'female', 
    //         state: 'Punjab', 
    //         bio: 'Libra', 
    //         role: {rolename:'Admin'
    //     }
    //     };
    //     fetch(url, {
    //         method: 'PUT', // or 'PUT'
    //         body: JSON.stringify(data), // data can be `string` or {object}!
    //         headers: {
    //             'Content-Type': 'application/json'
    //         }
    //     }).then(res => res.json())
    //         .then(response => console.log('Success:', JSON.stringify(response)))
    //         .catch(error => console.error('Error:', error));
    // }
    
    updateUser(){
        this.props.history.push('/editprofile')
    }

    render() {
         console.log(this.props.users)
        if(!this.props.users)
        {
            return(
                <></>
            )
        }

        let userList = this.props.users.map((user, user_id) => {
            return (

                <tr key={user.user_id}>

                    {/* <td>{user.user_id}</td> */}
                    <td><Link to={'/users/' + user.user_id}>{user.username}</Link></td>
                    {/* <td>{user.password}</td> */}
                    <td>{user.email}</td>
                    <td>{user.gender}</td>
                    <td>{user.state}</td>
                    {/* <td>{user.bio}</td> */}
                    <td><button className='mr-2' onClick={this.delete.bind(this, user.user_id)} type="button" className="btn btn-danger"><i class="fa fa-trash" aria-hidden="true"></i></button>
                     <button className ='mr-5'onClick={this.updateUser.bind(this)} type="button" className="btn btn-dark"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></button>
                    </td>
                </tr>

            )
        })


        return (
            <div>
                <div className="mb-3">
                    <div className={(this.props.message === '') ? '' : 'alert alert-success'} role="alert">
                        {this.props.message}
                    </div>
                </div>
                <h2 className='text-center'>User List</h2>
                <table className='table table-striped table-bordered'>
                    <thead>
                        <tr>
                            {/* <th>USERID</th> */}
                            <th>USERNAME</th>
                            {/* <th>Password</th> */}
                            <th>EMAIL</th>
                            <th>GENDER</th>
                            <th>STATE</th>
                            {/* <th>BIO</th> */}
                            <th>ACTIONS</th>
                        </tr>
                    </thead>
                    <tbody>
                        {userList}
                    </tbody>

                </table>
                </div>
                // {/* <div className="nav justify-content-start">
                //     {/* <button className ='mr-5'onClick={() => history.push('/Products')} type="button" className="btn btn-dark">Update</button> */}
                //     <Link className="nav-link " to="/userupdate">UPDATE USER</Link>
                // </div> */}
            
        )
    }


}
const mapStateToProps = (state, ownProps) => {
    return {
        message: state.userreducer.message,
        users: state.userreducer.users,
        history : ownProps.history
    }
}

const mapDispatchToProps = (dispatch) => {
    return {
        onFetchUsers: () => {
            console.log('debug info')
            return dispatch(actions.fetchUsers())
        },
        onDeleteUser: (user_id) => dispatch(actions.deleteUser(user_id)),
        // onUpdateUser: (payload) => dispatch(actions.updateUser(payload))
        // onFindUsers: (user_id) => { return dispatch(actions.findUsers(user_id)) }
    }
}

// export default ViewUsers;
export default connect(mapStateToProps, mapDispatchToProps)(Users);