
import {BASKET_MECI_BASE_URL} from './consts';

function status(response) {
    console.log('response status ' + response.status);
    if (response.status >= 200 && response.status < 300) {
        return Promise.resolve(response)
    } else {
        return Promise.reject(new Error(response.statusText))
    }
}

function json(response) {
    return response.json()
}

export function GetMeciuri() {
    var headers = new Headers();
    headers.append('Accept', 'application/json');
    var myInit = {method: 'GET', headers: headers, mode: 'cors'};
    var request = new Request(BASKET_MECI_BASE_URL, myInit);

    console.log('Inainte de fetch pentru ' + BASKET_MECI_BASE_URL);

    return fetch(request)
        .then(status)
        .then(json)
        .then(data => {
            console.log('Request succeeded with JSON response', data);
            return data;
        }).catch(error => {
            console.log('Request failed', error);
            return error;
        });
}

export function DeleteMeciuri(meciId) {
    console.log('inainte de fetch delete');
    var myHeaders = new Headers();
    myHeaders.append("Accept", "application/json");

    var antet = {method: 'DELETE', headers: myHeaders, mode: 'cors'};

    var meciDelUrl = BASKET_MECI_BASE_URL + '/' + meciId;

    return fetch(meciDelUrl, antet)
        .then(status)
        .then(response => {
            console.log('Delete status ' + response.status);
            return response.text();
        }).catch(e => {
            console.log('error ' + e);
            return Promise.reject(e);
        });
}

export function AddMeciuri(meci) {
    console.log('inainte de fetch post' + JSON.stringify(meci));

    var myHeaders = new Headers();
    myHeaders.append("Accept", "application/json");
    myHeaders.append("Content-Type", "application/json");

    myHeaders.append('Access-Control-Allow-Origin', 'http://localhost:5000');
    myHeaders.append('Access-Control-Allow-Origin', 'http://localhost:3000');
    myHeaders.append('Access-Control-Allow-Credentials', 'true');

    var antet = {method: 'POST', headers: myHeaders, mode: 'cors', body: JSON.stringify(meci)};

    return fetch(BASKET_MECI_BASE_URL, antet)
        .then(status)
        .then(response => {
            return response.text();
        }).catch(error => {
            console.log('Request failed', error);
            return Promise.reject(error);
        });
}

