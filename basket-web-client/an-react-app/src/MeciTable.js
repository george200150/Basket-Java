
import React from  'react';
import './MeciApp.css'

class MeciRow extends React.Component{
    handleClick=(event)=> {
        console.log('delete button pentru ' + this.props.meci.id);
        this.props.deleteFunc(this.props.meci.id);
    };

    render() {
        return (
            <tr>
                <td>{this.props.meci.id}</td>
                <td>{this.props.meci.home}</td>
                <td>{this.props.meci.away}</td>
                <td>{this.props.meci.date}</td>
                <td>{this.props.meci.tip}</td>
                <td>{this.props.meci.numarBileteDisponibile}</td>
                <td><button onClick={this.handleClick}>Delete</button></td>
            </tr>
        );
    }
}
/*<form onSubmit={this.handleClick}><input type="submit" value="Delete"/></form>*/
/**/
class MeciTable extends React.Component {
    render() {
        var rows = [];
        var functieStergere=this.props.deleteFunc;
        this.props.meciuri.forEach(function(meci) {

            rows.push(<MeciRow meci={meci} key={meci.id} deleteFunc={functieStergere} />);
        });
        return (<div className="MeciTable">

                <table className="center">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Home</th>
                        <th>Away</th>
                        <th>Date</th>
                        <th>NumarBileteDisponibile</th>

                        <th></th>
                    </tr>
                    </thead>
                    <tbody>{rows}</tbody>
                </table>

            </div>
        );
    }
}

export default MeciTable;