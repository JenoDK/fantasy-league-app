const $_documentContainer = document.createElement('template');

// language=HTML
$_documentContainer.innerHTML = `
	<custom-style>
		<style>
			.league-logo {
				margin: 10px;
			}

			@media (max-width: 400px) {
				.league-logo {
					display: none;
				}
			}
        </style>
    </custom-style>
`;

document.head.appendChild($_documentContainer.content);
