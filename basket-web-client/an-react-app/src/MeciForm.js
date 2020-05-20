import React from 'react';

class MeciForm extends React.Component {

    constructor(props) {
        super(props);
        //this.state = {id: '', home: '', away: '', date: '', tip: '', notix: ''};
        console.log("setup state MeciForm");
        this.state = {id: '', home: '', away: '', date: '', tip: '', numarBileteDisponibile: 0};

        //  this.handleChange = this.handleChange.bind(this);
        // this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleIdChange = (event) => {
        this.setState({id: event.target.value});
    };

    handleHomeChange = (event) => {
        this.setState({home: event.target.value});
    };

    handleAwayChange = (event) => {
        this.setState({away: event.target.value});
    };

    handleDateChange = (event) => {
        this.setState({date: event.target.value});
    };

    handleTipChange = (event) => {
        this.setState({tip: event.target.value});
    };

    handleNotixChange = (event) => {
        this.setState({numarBileteDisponibile: event.target.value});
    };

    handleSubmit =(event) => {
        var meci = {
            id: this.state.id,
            home: this.state.home,
            away: this.state.away,
            date: this.state.date,
            tip: this.state.tip,
            numarBileteDisponibile: this.state.numarBileteDisponibile
        };
        console.log('A match was submitted: ');
        console.log(meci);
        this.props.addFunc(meci);
        event.preventDefault();
    };

    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <label>
                    ID:
                    <input type="text" value={this.state.id} onChange={this.handleIdChange} />
                </label><br/>
                <label>
                    Home:
                    <input type="text" value={this.state.home} onChange={this.handleHomeChange} />
                </label><br/>
                <label>
                    Away:
                    <input type="text" value={this.state.away} onChange={this.handleAwayChange} />
                </label><br/>
                <label>
                    Date:
                    <input type="date" value={this.state.date} onChange={this.handleDateChange} />
                </label><br/>
                <label>
                    Type:
                    <select value={this.state.tip} onChange={this.handleTipChange} >
                        <option value="1">SAISPREZECIME</option>
                        <option value="2">OPTIME</option>
                        <option value="3">SFERT</option>
                        <option value="4">SEMIFINALA</option>
                        <option value="5">FINALA</option>
                    </select>

                </label><br/>
                <label>
                    Numar Bilete Disponibile:
                    <input type="text" value={this.state.numarBileteDisponibile} onChange={this.handleNotixChange} />
                </label><br/>

                <input type="submit" value="Submit" />
            </form>
        );
    }
}
export default MeciForm;