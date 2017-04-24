const camelcaseKeys = require('camelcase-keys');

/**
 * Base class to every MySQL Model.
 */
class Base {
	/**
	 * Save the model instance.
	 * @param {Object} Model - The model instance.
	 */
	constructor(Model) {
		this._Model = Model;
	}

	/**
	 * Insert an element to the database.
	 * @param {Object} attrs - Attributes with values of the element.
	 * @return {Promise} A Promise with the new element inserted.
	 */
	create(attrs) {
		return this._toJSON(new this._Model(attrs).save());
	}

	/**
	 * Return every element of associated with the Model.
	 * @return {Promise} A Promise with every element.
	 */
	getAll() {
		return this._toJSON(this._Model.fetchAll());
	}

	/**
	 * Select an element where Attributes equals.
	 * @param {Object} idOrAttrs - A number for an ID and an Object for different attributes.
	 * @return {Promise} A Promise with the element found.
	 */
	get(idOrAttrs) {
		if (typeof idOrAttrs === 'number') {
			return this._toJSON(this._Model.where('id', idOrAttrs).fetch());
		}
		return this._toJSON(this._Model.where(idOrAttrs).fetch());
	}

	/**
	 * Return a clean represention of an element added or fetched from the database.
	 * @param {Promise} req - The database request.
	 * @return {Promise} A Promise with a clean Array or Object.
	 */
	_toJSON(req) {
		return req.then((result) => {
			if (!result) {
				return null;
			}
			const resultJson = result.toJSON();
			if (Array.isArray(resultJson)) {
				resultJson.forEach((value, index, array) => {
					array[index] = camelcaseKeys(value);
				});
				return resultJson;
			}
			return camelcaseKeys(resultJson);
		});
	}
}

module.exports = Base;
