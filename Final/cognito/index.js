//import Amplify from 'aws-amplify';
var Amplify = require('aws-amplify');
var AmazonCognitoIdentity = new Amplify.AmazonCognitoIdentity();
//var AmazonCognitoIdentity = require('amazon-cognito-identity-js');

var authenticationData = {
    Username : 'DrAlex',
    Password : '111111',
};
var authenticationDetails = new AmazonCognitoIdentity.AuthenticationDetails(authenticationData);
var poolData = { UserPoolId : 'us-east-1_OX2cXFNYH',
    ClientId : '2ilqr0gkc8547nnve4g6eftgvj'
};
var userPool = new AmazonCognitoIdentity.CognitoUserPool(poolData);
var userData = {
    Username : 'username',
    Pool : userPool
};
var cognitoUser = new AmazonCognitoIdentity.CognitoUser(userData);
cognitoUser.authenticateUser(authenticationDetails, {
    onSuccess: function (result) {
        var accessToken = result.getAccessToken().getJwtToken();

        /* Use the idToken for Logins Map when Federating User Pools with identity pools or when passing through an Authorization Header to an API Gateway Authorizer */
        var idToken = result.idToken.jwtToken;
        console.log(idToken);
    },

    onFailure: function(err) {
        alert(err);
    },

});