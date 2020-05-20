import React from "react";
import MeciTable from './MeciTable';
import './MeciApp.css';
import {AddMeciuri, DeleteMeciuri, GetMeciuri} from "./utils/rest-calls";
import MeciForm from "./MeciForm";

class MeciApp extends React.Component{
    constructor(props){
        super(props);
        this.state={meciuri:[{"id":"-69","home":"-6920","away":"-6930","date":"2020-03-20","tip":"SFERT","numarBileteDisponibile":6912}],
            deleteFunc:this.deleteFunc.bind(this),
            addFunc:this.addFunc.bind(this),
    };
        console.log('MeciApp constructor')
    }

    addFunc(meci) {
        console.log('inside add Func ' + meci);
        AddMeciuri(meci)
            .then(res => GetMeciuri())
            .then(meciuri => this.setState({meciuri}))
            .catch(error => console.log('eroare add ', error));
    }

    deleteFunc(meci) {
        console.log("inside deleteFunc " + meci);
        DeleteMeciuri(meci)
            .then(res => GetMeciuri())
            .then(meciuri => this.setState({meciuri}))
            .catch(error => console.log('eroare delete', error));
    }

    componentDidMount() {
        console.log('inside componentDidMount');
        GetMeciuri().then(meciuri=>this.setState({meciuri}));
    }

    render(){
        return(
            <div className="MeciApp">
                <h1>Basket Matches Management</h1>
                <MeciForm addFunc={this.state.addFunc}/>
                <br/>
                <br/>
                <MeciTable meciuri={this.state.meciuri} deleteFunc={this.state.deleteFunc}/>
            </div>
        );
    }
}

export default MeciApp;