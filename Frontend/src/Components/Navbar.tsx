import { NavLink } from 'react-router-dom';

const Navbar = () => {
  return (
    <nav className="bg-gray-900 py-4 shadow-lg">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 flex justify-between items-center">
        {/* Logo o título */}
        <div className="text-white font-semibold text-lg">
          <NavLink to="/">GPTApp</NavLink>
        </div>

        {/* Enlaces de Login y Register */}
        <div className="flex space-x-6">
          <NavLink to="/" className="text-white no-underline hover:text-gray-300">
            Login
          </NavLink>
          <NavLink to="/register" className="text-white no-underline hover:text-gray-300">
            Register
          </NavLink>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;