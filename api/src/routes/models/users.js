const bookshelf = require('./bookshelf');
const Base = require('./Base');

const Users = bookshelf.Model.extend({
	tableName: 'users',
});

module.exports = new Base(Users);
