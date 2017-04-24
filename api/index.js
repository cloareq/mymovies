const corsHeaders = require('hapi-cors-headers');
const Glue = require('glue');
const good = require('good');
const logger = require('winston-lludol');
const Promise = require('bluebird');

const manifest = require('./manifest.json');

global.Promise = Promise;
global.logger = logger;

const options = {
	relativeTo: __dirname,
};

Glue.compose(manifest, options).then((server) =>
	Promise.all([
		server,
		server.register({
			register: good,
			options:  {
				reporters: {
					winston: [{
						module: 'hapi-good-winston',
						name:   'goodWinston',
						args:   [
							logger, {
								levels: {
									response: 'info',
									error:    'error',
								},
							},
						],
					}],
				},
			},
		}),
	])
).then(([server]) => {
	server.ext('onPreResponse', corsHeaders);

	server.start(() => {
		logger.info(`API listening at ${server.connections[0].info.uri}`);
	});
}).catch((error) => {
	logger.error(error);
});
