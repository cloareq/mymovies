const Joi = require('joi');
const handler = require('./handler');

const routes = [
	{
		method:  'POST',
		path:    '/movies/search',
		handler: handler.searchMovie,
		config:  {
			validate: {
				payload: {
					idfb: Joi.string().min(1).required(),
					search: Joi.string().min(1).required(),
				},
			},
		},
	},
	{
		method:  'POST',
		path:    '/movies/{idfb}/{idMovie}',
		handler: handler.updateMovie,
		config:  {
			validate: {
				payload: {
					mark:    Joi.number().min(1).max(5),
					comment: Joi.string().optional(),
				},
			},
		},
	},
	{
		method:  'GET',
		path:    '/movies/{idfb}/{idMovie}',
		handler: handler.getMovie,
	},
	{
		method:  'GET',
		path:    '/movies/{idfb}',
		handler: handler.getMovies,
	},
];

exports.register = function (server, options, next) {
	server.route(routes);
	next();
};

exports.register.attributes = {
	name:    'api.movies',
	version: '0.0.1',
};
