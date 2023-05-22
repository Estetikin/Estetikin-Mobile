const fs = require('fs');

const uploadHandler = async (request, h) => {
  const data = request.payload;

  if (!data.file) {
    //IF NO FILE IS INCLUDED IN POST
    return h.response('File not found').code(400);
  }

  const file = data.file;
  const filename = file.hapi.filename;
  //WILL BE CHANGED TO GCLOUD STORAGE LATER, STILL USING LOCAL FOLDER
  const path = __dirname + '/uploads/' + filename;

  const fileStream = fs.createWriteStream(path);

  file.on('error', (err) => {
    console.error(err);
    throw err;
  });

  file.pipe(fileStream);

  return new Promise((resolve, reject) => {
    file.on('end', () => {
      const response = h.response({ message: 'File uploaded successfully' });
      response.code(201);
      resolve(response);
    });

    fileStream.on('error', (err) => {
      console.error(err);
      reject(err);
    });
  });
};

module.exports = { uploadHandler };