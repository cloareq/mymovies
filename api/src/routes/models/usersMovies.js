const bookshelf = require('./bookshelf');
const Base = require('./Base');

const UsersMovies = bookshelf.Model.extend({
	tableName: 'users_movies',
});

module.exports = new Base(UsersMovies);
