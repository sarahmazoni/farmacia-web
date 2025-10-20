<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <title> Farmacia Flower </title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(to right, #c71585, #ffe6f0);
            min-height: 100vh;
            display: flex;
            flex-direction: column;
        }
        .navbar {
            background-color: #c71585;
        }
        .navbar-brand, .nav-link, .navbar-toggler-icon {
            color: white !important;
        }
        footer {
            margin-top: auto;
            background-color: #c71585;
            color: white;
            padding: 10px 0;
        }
        .card {
            background-color: #fff;
        }
        .card:hover {
            transform: scale(1.03);
            transition: transform 0.3s ease;
        }
        .btn-custom {
            background-color: #ff1493;
            border-color: #ff1493;
            color: white;
        }
        .btn-custom:hover {
            background-color: #c71585;
            border-color: #c71585;
            color: white;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg">
        <div class="container">
            <a class="navbar-brand fw-bold" href="#">Farmacia FLOWER</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#menu" aria-controls="menu" aria-expanded="false" aria-label="Menu">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="menu">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item"><a class="nav-link" href="ClienteServlet?action=list">Clientes</a></li>
                    <li class="nav-item"><a class="nav-link" href="PedidoServlet?action=list">Pedidos</a></li>
                    <li class="nav-item"><a class="nav-link" href="ProdutoServlet?action=list">Produtos</a></li>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container py-5 text-center" style="color: #ffffff;">
        <h1 class="mb-4 fw-bold">Bem-vindo ao Farmacia FLOWER 🌺 </h1>
        <p class="lead mb-5">Gerencie medicamentos, clientes e pedidos de forma simples e eficiente.</p>

        <div class="row g-4">
            <!-- Card Produtos -->
            <div class="col-md-4">
                <div class="card shadow-lg">
                    <div class="card-body">
                        <h5 class="card-title">Produtos</h5>
                        <p class="card-text">Cadastre, atualize e consulte medicamentos disponíveis na farmácia.</p>
                        <a href="ProdutoServlet?action=list" class="btn btn-custom">Gerenciar</a>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card shadow-lg">
                    <div class="card-body">
                        <h5 class="card-title">Clientes</h5>
                        <p class="card-text">Mantenha o cadastro dos clientes atualizado e organizado.</p>
                        <a href="ClienteServlet?action=list" class="btn btn-custom">Gerenciar</a>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="card shadow-lg">
                    <div class="card-body">
                        <h5 class="card-title">Pedidos</h5>
                        <p class="card-text">Controle os pedidos realizados e automatize processos de venda.</p>
                        <a href="PedidoServlet?action=list" class="btn btn-custom">Gerenciar</a>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <footer class="text-center">
        <p>&copy; 2025 Farmacia FLOWER - Todos os direitos reservados</p>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
