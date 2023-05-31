const Hapi = require('@hapi/hapi');
const HapiJwt = require('hapi-auth-jwt2');
const Jwt = require('jsonwebtoken');
const { uploadHandler } = require ('./handler');
//APP Secret Key for jwt
const secretKey = 'CodeGeniuses';
//USER (Will Be Replaced with DB)
const users = [
    { id: 1, username: 'Evan', password: 'konteks1' },
    { id: 2, username: 'Jotham', password: 'konteks2' },
];
const validate = async (decoded, request, h)=> {
    //VALIDATING USER
    const user = users.find ((u) => u.id === decoded.id);
    if (!user){
        return {isValid : false};
    }
    return{isValid : true};
};

const init = async () => {
    const server = Hapi.server ({
        port: 3000,
        host: 'localhost',
    });
    await server.register(HapiJwt);
    server.auth.strategy('jwt', 'jwt',{
        key: secretKey,
        validate,
        verifyOptions: {algorithms: ['HS256']},
    });
    server.auth.default('jwt');
    server.route([
        {
            //API FOR LOGIN USER (SOON WILL BE DB QUERY FIND (USER))
            method : 'POST',
            path : '/login',
            handler : async (request, h) => {
                const {username, password} = request.payload;
                const user = users.find ((u) => u.username === username && u.password === password);
                if (!user) {
                    return { message: 'Invalid credetials' };
                }
                //GENERATING TOKEN
                const token = Jwt.sign({id: user.id}, secretKey, {expiresIn:'3h'});
                //GOTTA FIND WAYS TO MAKE TOKEN LONGER BUT SAVE/secure
                return {token};
            },
            options: {
                auth: false, //AUTH DISABLED FOR THIS ROUTE (USE THIS IN CASE U DONT NEED TOKEN AUTHORIZATION)
            },
        },
        {
            //API FOR REGISTER
            method: 'POST',
            path: '/register',
            handler : async (request, h) => {
                const {email, username, password} = request.payload;
                if (!email || !username || !password) {
                    return { message: 'Invalid credetials' };
                }
                //adding to db (not-yet)
                //checking if username/email exist
                //GENERATING TOKEN
                // const token = Jwt.sign({id: user.id}, secretKey, {expiresIn:'3h'});
                // //GOTTA FIND WAYS TO MAKE TOKEN LONGER BUT SAVE/secure
                // return {token};
                return {message: 'Register Succesful'};
            },
            options: {
                auth: false, //AUTH DISABLED FOR THIS ROUTE (USE THIS IN CASE U DONT NEED TOKEN AUTHORIZATION)
            },


        },
        {
            //TESTING AUTHORIZATION
            method : 'GET',
            path : '/shield',
            handler: (request, h) => {
                return {message: 'Protected Route'};
            },
        },
        {
            //FILE UPLOAD API
            method: 'POST',
            path: '/upload',
            options: {
              payload: {
                output: 'stream',
                parse: true,
                multipart: true,
                maxBytes: 10 * 1024 * 1024, // 10MB limit
              },
            },
            handler: uploadHandler,
          },
    ]);
    await server.start();
    console.log('Running on %s', server.info.uri);
};
process.on('unhandledRejection', (err) => {
    console.log(err);
    process.exit(1);
  });
init();