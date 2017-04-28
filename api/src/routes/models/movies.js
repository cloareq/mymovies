const bookshelf = require('./bookshelf');
const Base = require('./Base');

const Movies = bookshelf.Model.extend({
	tableName: 'movies',
});

module.exports = new Base(Users);
