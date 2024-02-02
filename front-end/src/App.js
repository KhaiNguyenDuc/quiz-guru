import "./App.css";
import useRouteElements from './routes/index.js'

function App() {
  const routeElements = useRouteElements()
  return (
    <div className="App">
     {routeElements}
    </div>
  );
}

export default App;
